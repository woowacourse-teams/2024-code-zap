// import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
// import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

// import { Flex, Text } from '@/components';
// import type { TemplateListItem } from '@/types';
// import { formatRelativeTime, getLanguageByFilename } from '@/utils';

// interface Props {
//   item: TemplateListItem;
// }

// const TemplateItem = ({ item }: Props) => {
//   const { title, modifiedAt, thumbnail } = item;

//   return (
//     <Flex direction='column' gap='1.6rem' width='100%'>
//       <Flex direction='column' justify='flex-start' align='flex-start' width='100%' gap='0.8rem'>
//         <Text.Large color='white'>{title}</Text.Large>
//       </Flex>
//       <Flex direction='column'>
//         <Flex
//           align='center'
//           height='3rem'
//           padding='1rem 1.5rem'
//           style={{ background: '#393e46', borderRadius: '8px 8px 0 0' }}
//         >
//           <Text.Small color='#fff' weight='bold'>
//             {thumbnail.filename}
//           </Text.Small>
//         </Flex>
//         <SyntaxHighlighter
//           language={getLanguageByFilename(thumbnail.filename)}
//           style={vscDarkPlus}
//           showLineNumbers={true}
//           customStyle={{ margin: 0, borderRadius: '0 0 8px 8px', width: '100%', tabSize: 2 }}
//           codeTagProps={{
//             style: {
//               fontSize: '1rem',
//             },
//           }}
//         >
//           {thumbnail.content}
//         </SyntaxHighlighter>
//       </Flex>

//       <Text.Small color='white'>{formatRelativeTime(modifiedAt)}</Text.Small>
//     </Flex>
//   );
// };

// export default TemplateItem;
