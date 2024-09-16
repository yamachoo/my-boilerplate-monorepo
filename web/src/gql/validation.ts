import * as v from 'valibot'
import type { RegisterUserInput } from './graphql'


export function RegisterUserInputSchema(): v.GenericSchema<RegisterUserInput> {
  return v.object({
    email: v.pipe(v.string(), v.nonEmpty(), v.email()),
    username: v.pipe(v.string(), v.minLength(1), v.maxLength(100))
  })
}
