# java-graphql-annotations

Yet another graphql annotation processor based on https://github.com/leangen/graphql-spqr.

## Optionals

* `GraphQLOutputType`s can only be described with `Optional`.
* `GraphQLInputType`s can be described with `Optional` or any other `Optional` alternative registered.
  Usually `Alternative` which better handles the `undefined` type.
  
  
  
## Query and mutation methods

This library allows automatic injection of parameters into query and mutation methods. There are two types
of parameters, those that are part of the context of the application (controlled by the server) and those that
are inputs (controlled by the client). As a consumer of the server, you can only choose from the available 
context parameters, but you can create whatever input parameters you want.

All parameters in the method must be annotated, either with `GraphQLContext` or `GraphQLArgument(name = '...')`. In
cases where there are two equal context classes you can further specify them with `GraphQLContext(name = '...')`.


### Configuration

While build the schema you must register all the data fetchers that are part of the context. The registration signatures are:

```
public <T> GraphSchemaBuilder withContext(Class<T> context, DataFetcher<T> fetcher)
public <T> GraphSchemaBuilder withContext(Class<T> context, String name, DataFetcher<T> fetcher)
```

Which is usually invoked as 

```
builder.withContext(PlasmaTransaction.class,
    environment -> 
    ((YourContext) environment.getContext()).getTransaction()
);
```

You can register as many contexts as you want and there are no preloaded contexts. You can further expose
your complete environment or context configuring the builder as follows:

```
builder.withContext(DataFetchingEnviromnent.class,
    environment -> environment
);
builder.withContext(YourContext.class,
    environment -> (YourContext) environment.getContext()
);
```
