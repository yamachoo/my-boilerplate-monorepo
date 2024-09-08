import { z } from 'zod'
import { RegisterUserInput } from './graphql'

type Properties<T> = Required<{
  [K in keyof T]: z.ZodType<T[K], any, T[K]>;
}>;

type definedNonNullAny = {};

export const isDefinedNonNullAny = (v: any): v is definedNonNullAny => v !== undefined && v !== null;

export const definedNonNullAnySchema = z.any().refine((v) => isDefinedNonNullAny(v));

export function RegisterUserInputSchema(): z.ZodObject<Properties<RegisterUserInput>> {
  return z.object({
    email: z.string(),
    username: z.string().min(1).max(100)
  })
}
