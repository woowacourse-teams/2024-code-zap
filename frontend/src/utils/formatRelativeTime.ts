export const formatRelativeTime = (dateString: string, now: Date = new Date()) => {
  const date = new Date(dateString);
  const { diffInMinutes, diffInHours } = calculateTimeDifference(date, now);

  if (diffInMinutes < 10) {
    return '방금 전';
  }

  if (diffInMinutes < 60) {
    return `${Math.floor(diffInMinutes)}분 전`;
  }

  if (diffInHours < 24) {
    return `${Math.floor(diffInHours)}시간 전`;
  }

  return formatDate(dateString);
};

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();

  return `${year}년 ${month}월 ${day}일`;
};

const calculateTimeDifference = (date: Date, now: Date) => {
  const diffInMilliseconds = now.getTime() - date.getTime();
  const diffInMinutes = diffInMilliseconds / (1000 * 60);
  const diffInHours = diffInMinutes / 60;

  return { diffInMinutes, diffInHours };
};
