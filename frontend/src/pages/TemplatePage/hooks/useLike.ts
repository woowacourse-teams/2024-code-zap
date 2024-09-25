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
    try {
      if (!isLiked) {
        setIsLiked(true);
        setLikesCount((prev) => prev + 1);
        await likeTemplate(templateId);
      } else {
        setIsLiked(false);
        setLikesCount((prev) => prev - 1);
        await dislikeTemplate(templateId);
      }
    } catch (error) {
      setLikesCount((prev) => prev + (isLiked ? -1 : 1));
      setIsLiked(initialIsLiked);
    }
  };

  return {
    likesCount,
    isLiked,
    toggleLike,
  };
};
