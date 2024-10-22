export const getBrowser = () => {
  const browsers = [
    'Chrome',
    'Opera',
    'WebTV',
    'Whale',
    'Beonex',
    'Chimera',
    'NetPositive',
    'Phoenix',
    'Firefox',
    'Safari',
    'SkipStone',
    'Netscape',
    'Mozilla',
  ];

  const userAgent = window.navigator.userAgent.toLowerCase();
  //TODO: 모바일 환경 실험 필요

  if (userAgent.includes('edg')) {
    return 'Edge';
  }

  if (userAgent.includes('trident') || userAgent.includes('msie')) {
    return 'Internet Explorer';
  }

  return browsers.find((browser) => userAgent.includes(browser.toLowerCase())) || 'Other';
};
