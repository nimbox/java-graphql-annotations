# java-graphql-annotations

Yet another graphql annotation processor based on https://github.com/leangen/graphql-spqr.

## Optionals

* `GraphQLOutputType`s can only be described with `Optional`.
* `GraphQLInputType`s can be described with `Optional` or any other `Optional` alternative registered.
  Usually `Alternative` which better handles the `undefined` type.
  