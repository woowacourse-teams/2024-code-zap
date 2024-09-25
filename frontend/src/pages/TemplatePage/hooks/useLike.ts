import { useEffect, useState } from 'react';

import { useDislikeMutation, useLikeMutation } from '@/queries/likes';

interface UseLikeProps {
  templateId: number;
  initialLikesCount: number;
  initialIsLiked: boolean;
}

export const useLike = ({ templateId, initialLikesCount, initialIsLiked }: UseLikeProps) => {
  const [likesCount, setLikesCount] = useState(initialLikesCount);
  const [isLiked, setIsLiked] = useState(initialIsLiked);

  const { mutateAsync: likeTemplate } = useLikeMutation();
  const { mutateAsync: dislikeTemplate } = useDislikeMutation();

  useEffect(() => {
    setLikesCount(initialLikesCount);
    setIsLiked(initialIsLiked);
  }, [initialLikesCount, initialIsLiked]);

  const toggleLike = async () => {
    const prevLikesCount = likesCount;
    const prevIsLiked = isLiked;

    try {
      if (!isLiked) {
        setIsLiked(true);
        setLikesCount(initialLikesCount + 1 - +initialIsLiked);
        await likeTemplate(templateId);
      } else {
        setIsLiked(false);
        setLikesCount(initialLikesCount - 1 + +!initialIsLiked);
        await dislikeTemplate(templateId);
      }
    } catch (error) {
      setLikesCount(prevLikesCount);
      setIsLiked(prevIsLiked);
    }
  };

  return {
    likesCount,
    isLiked,
    toggleLike,
  };
};
