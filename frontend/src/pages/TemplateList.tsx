import { Flex } from '@/components/Flex';
import { Link } from 'react-router-dom';
import { TemplateItem } from '@/components/TemplateItem';
import { Text } from '@/components/Text';
import useTemplateListQuery from '@/hooks/useTemplateListQuery';

const TemplateList = () => {
  const { data, error, isLoading } = useTemplateListQuery();
  console.log(data, error, isLoading);

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error: { error.message }</div>;

  const list = data?.templates || [];

  console.log(list);

  return (
    <Flex
      direction="column"
      justify="flex-start"
      align="flex-end"
      width="100%"
      padding="10rem 0 0 0"
      gap="3.6rem"
    >
      <Text.Body weight="bold">{ list.length } Results</Text.Body>
      <Flex direction="column" width="100%" gap="4.8rem">
        { list.map((item) => (
          <Link to={ `templates/${item.id}` } key={ item.id }>
            <TemplateItem item={ item } />
          </Link>
        )) }
      </Flex>
    </Flex>
  );
};

export default TemplateList;
