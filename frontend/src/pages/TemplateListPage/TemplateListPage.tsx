import { Link } from 'react-router-dom';

import { Flex, TemplateItem, Text } from '@/components';
import { useTemplateListQuery } from '@/hooks/template';

const TemplateListPage = () => {
  const { data, error, isLoading } = useTemplateListQuery();

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error.message}</div>;
  }

  const list = data?.templates || [];

  return (
    <Flex direction='column' justify='flex-start' align='flex-end' width='100%' padding='10rem 0 0 0' gap='3.6rem'>
      <Text.Medium color='white' weight='bold'>
        {list.length} Results
      </Text.Medium>
      <Flex direction='column' width='100%' gap='4.8rem'>
        {list.map((item) => (
          <Link to={`templates/${item.id}`} key={item.id}>
            <TemplateItem item={item} />
          </Link>
        ))}
      </Flex>
    </Flex>
  );
};

export default TemplateListPage;
