import { TAG_COLORS } from '../style/tagColors';

export const getTagColor = (id: number) => TAG_COLORS[id % TAG_COLORS.length];
