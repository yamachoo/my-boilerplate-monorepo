/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_GRAPHQL_SERVER_ENDPOINT: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
