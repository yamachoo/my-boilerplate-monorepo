import { Routes } from '@generouted/react-router'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'

const queryClient = new QueryClient()

// biome-ignore lint/style/noNonNullAssertion: This is a root element, it should always exist
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <Routes />
      <ReactQueryDevtools />
    </QueryClientProvider>
  </StrictMode>,
)
