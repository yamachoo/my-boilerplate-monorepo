import type { CodegenConfig } from '@graphql-codegen/cli'

const config: CodegenConfig = {
  overwrite: true,
  schema: '../server/src/main/resources/graphql/schema.graphqls',
  documents: 'src/**/*.tsx',
  ignoreNoDocuments: true,
  generates: {
    'src/gql/': {
      preset: 'client',
    },
  },
}

export default config
