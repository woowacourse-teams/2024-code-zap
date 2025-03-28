/* eslint-disable import/no-named-as-default */
/* eslint-disable import/no-named-as-default-member */
import i18n from 'i18next';

export const formatRelativeTime = (
  dateString: string,
  now: Date = new Date(),
) => {
  const date = new Date(dateString);
  const { diffInMinutes, diffInHours } = calculateTimeDifference(date, now);

  if (diffInMinutes < 10) {
    return i18n.t('FormatRelativeTime:justNow');
  }

  if (diffInMinutes < 60) {
    return i18n.t('FormatRelativeTime:minutesAgo', {
      minutes: Math.floor(diffInMinutes),
    });
  }

  if (diffInHours < 24) {
    return i18n.t('FormatRelativeTime:hoursAgo', {
      hours: Math.floor(diffInHours),
    });
  }

  return formatDate(dateString);
};

const formatDate = (dateString: string, locale = i18n.language) =>
  new Intl.DateTimeFormat(locale, {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  }).format(new Date(dateString));

const calculateTimeDifference = (date: Date, now: Date) => {
  const diffInMilliseconds = now.getTime() - date.getTime();
  const diffInMinutes = diffInMilliseconds / (1000 * 60);
  const diffInHours = diffInMinutes / 60;

  return { diffInMinutes, diffInHours };
};
