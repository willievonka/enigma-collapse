# https://docs.langchain.com/oss/python/langchain/knowledge-base
import httpx
from langchain_community.document_loaders import TextLoader
from langchain_openai import OpenAIEmbeddings
from langchain_qdrant import QdrantVectorStore
from langchain_text_splitters import RecursiveCharacterTextSplitter
from pydantic import SecretStr

from rag.config import TXT_PATH, OPENROUTER_MODEL, OPENROUTER_API_KEY, QDRANT_COLLECTION, QDRANT_URL, \
    OPENROUTER_API_URL, HTTP_PROXY

# === 1. Load TXT ===
loader = TextLoader(TXT_PATH, encoding="utf-8")
documents = loader.load()

# === 2. Split into chunks ===
text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=1000,
    chunk_overlap=200
)

docs = text_splitter.split_documents(documents)
print(f'Total chunks: {len(docs)}')

# === 3. Embedding model ===
http_client = httpx.Client(proxy=HTTP_PROXY) # Прокси

embeddings = OpenAIEmbeddings(
    model=OPENROUTER_MODEL,
    base_url=OPENROUTER_API_URL,
    api_key=SecretStr(OPENROUTER_API_KEY),
    http_client=http_client
)

# ========= 4. Upload =========
vector_store = QdrantVectorStore.from_documents(
    documents=docs,
    embedding=embeddings,
    url=QDRANT_URL,
    collection_name=QDRANT_COLLECTION
)

print("Uploaded to Qdrant 🚀")
