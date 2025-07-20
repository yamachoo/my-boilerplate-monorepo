# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Structure

This is a monorepo containing:
- **server/**: Kotlin Spring Boot GraphQL API using DGS Framework
- **web/**: React SPA with TypeScript, using Vite and React Router
- **db/**: Database migrations managed by dbmate

## Common Commands

### Project Setup
```bash
task init  # Initialize project, install hooks, start Docker, run migrations
```

### Development
```bash
# Start infrastructure
task docker:up

# Database operations
task db:migrate        # Run pending migrations
task db:rollback       # Roll back last migration
task db:new -- name    # Create new migration
task db:status         # Check migration status

# Server (from server/ directory)
./gradlew bootRun                    # Start development server
./gradlew test                       # Run tests
./gradlew spotlessApply             # Format code
./gradlew detekt                    # Run static analysis
./gradlew generateJava              # Generate GraphQL types

# Web (from web/ directory)
pnpm dev                # Start dev server with GraphQL codegen watch
pnpm build              # Build for production
pnpm check              # Lint and format check
pnpm fix                # Auto-fix linting issues
pnpm codegen            # Generate GraphQL client code
```

### Testing
```bash
# Server
cd server && ./gradlew test

# Web
cd web && pnpm test  # (if test script exists)
```

## Architecture

### Server (Kotlin/Spring Boot)
- **Domain Layer**: `domain/` - Core business entities and repositories
- **Handler Layer**: `handler/graphql/` - GraphQL resolvers (queries, mutations)
- **Infrastructure Layer**: `infra/` - Database implementations using Komapper ORM
- **Use Cases**: `usecase/` - Application business logic

### GraphQL Schema
- Schema files in `server/src/main/resources/graphql/`
- Custom scalars: Email, DateTime
- DGS codegen generates types from schema

### Web (React/TypeScript)
- File-based routing with `@generouted/react-router`
- GraphQL client with `@tanstack/react-query` and `graphql-request`
- Auto-generated GraphQL types and hooks from server schema
- Biome for linting and formatting

### Database
- MySQL with migrations in `db/migrations/`
- Komapper ORM for type-safe database access
- TestContainers for integration testing

## Code Quality Tools

### Pre-commit Hooks (Lefthook)
- **server-format**: Spotless + Detekt
- **web-format**: Biome fix
- **sql-format**: SQLFluff

### Linting/Formatting
- **Kotlin**: Spotless (ktlint) + Detekt
- **TypeScript**: Biome
- **SQL**: SQLFluff
- **GraphQL**: Prettier

## Key Dependencies

### Server
- Spring Boot 3.x with GraphQL
- DGS Framework for GraphQL
- Komapper for database access
- Kotest for testing

### Web
- React 19 with TypeScript
- Vite for bundling
- TanStack Query for state management
- GraphQL Code Generator for type generation