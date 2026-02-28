import { Category } from '../../../enums/category.enum';

export const CATEGORY_COLOR: Record<Category, string> = {
    [Category.Maintenance]: '#00cca3',
    [Category.Modernization]: '#5f52be',
    [Category.Diagnostics]: '#0984E3',
    [Category.NodeReplacement]: '#E17055',

    [Category.Approval]: '#A29BFE',
    [Category.Reporting]: '#636E72',
    [Category.Audit]: '#2D3436',

    [Category.CriticalError]: '#D63031',
    [Category.Warning]: '#FDCB6E',
    [Category.Risk]: '#E84393',

    [Category.Setup]: '#00CEC9',
    [Category.Calibration]: '#F39C12',
    [Category.Conservation]: '#95A5A6',
    [Category.Testing]: '#74B9FF',

    [Category.Unknown]: '#B2BEC3',
};
