# === 1. Embeddings ===
import httpx
from langchain_openai import OpenAIEmbeddings
from langchain_qdrant import QdrantVectorStore
from pydantic import SecretStr
from qdrant_client import QdrantClient

from rag.config import OPENROUTER_MODEL, OPENROUTER_API_KEY, OPENROUTER_API_URL, QDRANT_URL, QDRANT_COLLECTION, \
    HTTP_PROXY

# === 1. Embeddings ===
http_client = httpx.Client(proxy=HTTP_PROXY) # Прокси

embeddings = OpenAIEmbeddings(
    model=OPENROUTER_MODEL,
    base_url=OPENROUTER_API_URL,
    api_key=SecretStr(OPENROUTER_API_KEY),
    http_client=http_client,
)

# === 2. Connect Qdrant ===
client = QdrantClient(
    url=QDRANT_URL
)

# === 2. Connect to existing collection ===
vector_store = QdrantVectorStore(
    client=client,
    collection_name=QDRANT_COLLECTION,
    embedding=embeddings,
)

# === 3. User query ===
query = input("Введите запрос: ")

# === 4. Search ===
results = vector_store.similarity_search(
    query,
    k=5
)

# === 5. Print ===
print("\n--- Найденные чанки ---\n")

for i, doc in enumerate(results):
    print(f"Chunk {i + 1}:\n")
    print(doc.page_content)
    print("\n----------------------\n")
