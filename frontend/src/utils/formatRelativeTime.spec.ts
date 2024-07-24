import { formatRelativeTime } from './formatRelativeTime';

describe('formatRelativeTime', () => {
  test('date가 현재 시간과 비교하여 10분 이내 차이가 나는 경우 `방금 전`을 리턴한다.', () => {
    const now = new Date('2024-01-01T12:00:00');
    const date = new Date('2024-01-01T11:55:00');

    expect(formatRelativeTime(date.toISOString(), now)).toBe('방금 전');
  });
  test('date가 현재 시간과 비교하여 1시간 이내 차이가 나는 경우 `몇 분 전`을 리턴한다.', () => {
    const now = new Date('2024-01-01T12:00:00');
    const date = new Date('2024-01-01T11:30:00');

    expect(formatRelativeTime(date.toISOString(), now)).toBe('30분 전');
  });
  test('date가 현재 시간과 비교하여 24시간 이내 차이가 나는 경우 `몇 시간 전`을 리턴한다.', () => {
    const now = new Date('2024-01-01T12:00:00');
    const date = new Date('2024-01-01T10:00:00');

    expect(formatRelativeTime(date.toISOString(), now)).toBe('2시간 전');
  });
  test('date가 현재 시간과 비교하여 24시간 이상 차이가 나는 경우 `몇 년 몇 월 며칠`을 리턴한다.', () => {
    const now = new Date('2024-01-03T12:00:00');
    const date = new Date('2024-01-01T12:00:00');

    expect(formatRelativeTime(date.toISOString(), now)).toBe('2024년 1월 1일');
  });
});
