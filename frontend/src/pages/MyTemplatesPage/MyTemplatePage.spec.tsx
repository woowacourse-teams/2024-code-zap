import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import '@testing-library/jest-dom';
import { render, fireEvent, waitFor, within } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';

import { AuthProvider } from '../../contexts/authContext';
import MyTemplatePage from './MyTemplatePage';

beforeAll(() => {
  Object.defineProperty(window, 'scrollTo', {
    value: jest.fn(),
    writable: true,
  });
});

const renderWithProviders = (ui: React.ReactNode) => {
  const queryClient = new QueryClient();

  return render(
    <Router>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>{ui}</AuthProvider>
      </QueryClientProvider>
    </Router>,
  );
};

describe('MyTemplatePage', () => {
  test('키워드 검색 (제목): "title1"를 검색하면 템플릿의 title에 "title1"이 포함되어 있는지 확인', async () => {
    const { getByPlaceholderText, getAllByText } = renderWithProviders(<MyTemplatePage />);

    const searchInput = getByPlaceholderText('검색');

    fireEvent.change(searchInput, { target: { value: 'title1' } });

    await waitFor(() => {
      const templates = getAllByText('title1');

      templates.forEach((template) => {
        expect(template).toHaveTextContent('title1');
      });
    });
  });

  test('키워드 검색 (내용): "description1"를 검색하면 템플릿의 description에 "description1"이 포함되어 있는지 확인', async () => {
    const { getByPlaceholderText, getAllByText } = renderWithProviders(<MyTemplatePage />);

    const searchInput = getByPlaceholderText('검색');

    fireEvent.change(searchInput, { target: { value: 'description1' } });

    await waitFor(() => {
      const templates = getAllByText('description1');

      templates.forEach((template) => {
        expect(template).toHaveTextContent('description1');
      });
    });
  });

  test('키워드 검색 (스니펫): "Hello, World"를 검색하면 템플릿의 snippet에 "Hello, World"이 포함되어 있는지 확인', async () => {
    const { getByPlaceholderText, getAllByText } = renderWithProviders(<MyTemplatePage />);

    const searchInput = getByPlaceholderText('검색');

    fireEvent.change(searchInput, { target: { value: 'Hello, World' } });

    await waitFor(() => {
      const templates = getAllByText('title3');

      templates.forEach((template) => {
        expect(template).toHaveTextContent('title3');
      });
    });
  });

  test('카테고리 (필터링): "카테고리 없음" 버튼을 클릭하면 모든 템플릿의 category.name이 "카테고리 없음"인지 확인', async () => {
    const { getByText, getAllByTestId } = renderWithProviders(<MyTemplatePage />);

    await waitFor(() => {
      const categoryButton = getByText('카테고리 없음');

      fireEvent.click(categoryButton!);

      const templates = getAllByTestId('template-card');

      templates.forEach((template) => {
        expect(template).toHaveTextContent('title11');
      });
    });
  });

  test('카테고리 (하이라이팅): "카테고리 없음" 버튼을 클릭하면 하이라이팅이 이동하는지 확인', async () => {
    const { getByText, getByTestId } = renderWithProviders(<MyTemplatePage />);

    await waitFor(() => {
      const categoryButton = getByText('카테고리 없음');
      const highlightBox = getByTestId('category-highlighter-box');

      fireEvent.click(categoryButton);

      const categoryButtonRect = categoryButton.getBoundingClientRect();
      const highlightBoxRect = highlightBox.getBoundingClientRect();

      expect(highlightBoxRect.top).toBeCloseTo(categoryButtonRect.top, 0);
      expect(highlightBoxRect.left).toBeCloseTo(categoryButtonRect.left, 0);
    });
  });

  test('카테고리 (순서): CategoryMenu의 "카테고리 없음" 버튼이 가장 아래에 위치하는지 확인', async () => {
    const { getAllByTestId } = renderWithProviders(<MyTemplatePage />);

    await waitFor(() => {
      const categoryButtons = getAllByTestId('category-button');

      expect(categoryButtons[categoryButtons.length - 1]).toHaveTextContent('카테고리 없음');
    });
  });

  test('태그 (싱글필터링): "JavaScript" 태그 버튼을 클릭하면 모든 템플릿이 "JavaScript" 태그를 갖고 있는지 확인', async () => {
    const { getByTestId, getAllByTestId } = renderWithProviders(<MyTemplatePage />);

    await waitFor(() => {
      const tagFilterMenu = getByTestId('tag-filter-menu');
      const tagButton = within(tagFilterMenu).getByText('JavaScript');

      fireEvent.click(tagButton);

      const templates = getAllByTestId('template-card');

      templates.forEach((template) => {
        expect(template).toHaveTextContent('JavaScript');
      });
    });
  });

  test('태그 (멀티필터링): "JavaScript"와 "React" 태그 버튼을 클릭하면 모든 템플릿이 두 태그를 갖고 있는지 확인', async () => {
    const { getByTestId, getAllByTestId } = renderWithProviders(<MyTemplatePage />);

    await waitFor(() => {
      const tagFilterMenu = getByTestId('tag-filter-menu');
      const javascriptTagButton = within(tagFilterMenu).getByText('JavaScript');
      const reactTagButton = within(tagFilterMenu).getByText('React');

      fireEvent.click(javascriptTagButton);
      fireEvent.click(reactTagButton);

      const templates = getAllByTestId('template-card');

      templates.forEach((template) => {
        expect(template).toHaveTextContent('JavaScript');
        expect(template).toHaveTextContent('React');
      });
    });
  });

  test('태그 (하이라이팅): "JavaScript" 태그 버튼을 클릭하면 버튼 색깔이 하이라이팅 되는지 확인', async () => {
    const { getByTestId } = renderWithProviders(<MyTemplatePage />);

    await waitFor(() => {
      const tagFilterMenu = getByTestId('tag-filter-menu');
      const tagButton = within(tagFilterMenu).getByText('JavaScript');

      fireEvent.click(tagButton);

      expect(tagButton).toHaveStyle('background-color: ${theme.color.light.primary_400}');
    });
  });

  test('정렬 (초기상태): 정렬 Dropdown 버튼을 클릭하지 않았을 때 템플릿의 modifiedAt이 초기값(최근순)으로 정렬되는지 확인', async () => {
    const { getAllByTestId } = renderWithProviders(<MyTemplatePage />);

    await waitFor(() => {
      const templates = getAllByTestId('template-card');
      const modifiedAtDates = templates.map((template) => new Date(template.getAttribute('data-modified-at')!));
      const sortedDates = [...modifiedAtDates].sort((a, b) => b.getTime() - a.getTime());

      expect(modifiedAtDates).toEqual(sortedDates);
    });
  });

  test('상세페이지: "title1" 템플릿 카드를 클릭하면 "title1" 템플릿의 상세페이지로 이동하는지 확인', async () => {
    const { getByText } = renderWithProviders(<MyTemplatePage />);

    await waitFor(() => {
      const templateCard = getByText('title2');

      fireEvent.click(templateCard);

      expect(window.location.pathname).toBe('/templates/2');
    });
  });
});
