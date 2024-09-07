import type { TypedDocumentNode } from '@graphql-typed-document-node/core'
import { type UseQueryResult, useQuery } from '@tanstack/react-query'
import request from 'graphql-request'

export function useGraphQL<TResult, TVariables>(
  document: TypedDocumentNode<TResult, TVariables>,
  ...[variables]: TVariables extends Record<string, never> ? [] : [TVariables]
): UseQueryResult<TResult> {
  return useQuery({
    // biome-ignore lint/suspicious/noExplicitAny: Ignored to make it a generic method
    queryKey: [(document.definitions[0] as any).name.value, variables],
    queryFn: async ({ queryKey }) =>
      request(
        import.meta.env.VITE_GRAPHQL_SERVER_ENDPOINT,
        document,
        queryKey[1] ? queryKey[1] : undefined,
      ),
  })
}
