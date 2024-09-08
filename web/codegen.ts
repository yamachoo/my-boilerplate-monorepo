import type { CodegenConfig } from '@graphql-codegen/cli'
import { z } from 'zod'

const config: CodegenConfig = {
  overwrite: true,
  schema: '../server/src/main/resources/graphql/schema.graphqls',
  documents: 'src/**/*.tsx',
  ignoreNoDocuments: true,
  generates: {
    'src/gql/': {
      preset: 'client',
      config: {
        strictScalars: true,
        scalars: {
          DateTime: 'Date',
        },
      },
    },
    'src/gql/validation.ts': {
      plugins: ['typescript-validation-schema'],
      config: {
        importFrom: './graphql',
        schema: 'zod',
        scalarSchemas: {
          DateTime: z.string().datetime({ offset: true }),
        },
        directives: {
          Size: {
            min: 'min',
            max: 'max',
          },
        },
      },
    },
  },
}

export default config
