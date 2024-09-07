import { Link } from '../router.ts'

export default function Home() {
  return (
    <>
      <h1>Home</h1>
      <p>
        <Link to="/app">App</Link>
      </p>
      <p>
        <Link to="/hello">Hello</Link>
      </p>
    </>
  )
}
