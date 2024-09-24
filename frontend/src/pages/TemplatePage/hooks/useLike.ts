import { useEffect, useState } from 'react';

import { useDislikeMutation, useLikeMutation } from '@/queries/likes';

interface UseLikeProps {
  templateId: number;
  initialLikeCount: number;
  initialIsLiked: boolean;
}

export const useLike = ({ templateId, initialLikeCount, initialIsLiked }: UseLikeProps) => {
  const [likeCount, setLikeCount] = useState(initialLikeCount);
  const [isLiked, setIsLiked] = useState(initialIsLiked);
  const [isProcessing, setIsProcessing] = useState(false);

  const { mutateAsync: likeTemplate } = useLikeMutation();
  const { mutateAsync: dislikeTemplate } = useDislikeMutation();

  useEffect(() => {
    setLikeCount(initialLikeCount);
    setIsLiked(initialIsLiked);
  }, [initialLikeCount, initialIsLiked]);

  const toggleLike = async () => {
    if (isProcessing) {
      return;
    }

    setIsProcessing(true);

    try {
      if (!isLiked) {
        setIsLiked(true);
        setLikeCount((prev) => prev + 1);
        await likeTemplate(templateId);
      } else {
        setIsLiked(false);
        setLikeCount((prev) => prev - 1);
        await dislikeTemplate(templateId);
      }
    } catch (error) {
      setLikeCount((prev) => prev + (isLiked ? -1 : 1));
      setIsLiked(initialIsLiked);
    } finally {
      setIsProcessing(false);
    }
  };

  return {
    likeCount,
    isLiked,
    toggleLike,
    isProcessing,
  };
};
