import type { CodegenConfig } from '@graphql-codegen/cli'

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
          ID: 'string',
          DateTime: 'Date',
          Email: 'string',
        },
      },
    },
    'src/gql/validation.ts': {
      plugins: ['typescript-validation-schema'],
      config: {
        importFrom: './graphql',
        useTypeImports: true,
        schema: 'valibot',
        scalarSchemas: {
          DateTime: 'v.pipe(v.string(), v.isoDateTime())',
          Email: 'v.pipe(v.string(), v.nonEmpty(), v.email())',
        },
        directives: {
          Size: {
            min: 'minLength',
            max: 'maxLength',
          },
        },
      },
    },
  },
}

export default config
