import { graphql } from '../gql'
import { useGraphQL } from '../hooks/use-graphql.ts'

const helloQueryDocument = graphql(`
  query helloQuery($name: String!) {
    hello(name: $name) 
  }
`)

export default function Hello() {
  const { data } = useGraphQL(helloQueryDocument, { name: 'yamachoo' })

  return (
    <>
      <p>{data?.hello}</p>
    </>
  )
}
