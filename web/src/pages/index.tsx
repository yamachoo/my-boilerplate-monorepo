import { Link } from '../router.ts'

export default function Home() {
  return (
    <>
      <h1>Home</h1>
      <Link to="/app">App</Link>
    </>
  )
}
