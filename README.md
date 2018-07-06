# Kubeflow Client

Under construction, **DO NOT USE**

## Generate client

This repo uses OpenAPI to generate library for languages other than go.

### Generate models

To update client using OpenAPI, you should generate golang model first:

```bash
cd $(GOPATH)/github.com/jetmuffin/tf-operator
hack/update-codegen.sh
```

Then, create swagger.json according to this model:

```bash
go run hack/genspec/main.go > $(KUBEFLOW_CLIENT_PATH)/api/openapi-spec/swagger.json
```

Since client generator reads spec from github, you need to update your generated `swagger.json` to your repo.

### Generate library

We use official tool `kubernetes-client/gen` to generate a client. There are several parameters required to be
set in the configuration file `<language>/settings`, set necessary parameters according to your environment.

```
KUBERNETES_BRANCH=master
CLIENT_VERSION=v1alpha2
PACKAGE_NAME=org.kubeflow.client
USERNAME=jetmuffin
REPOSITORY=kubeflow-client
```

Generate client by commands as follows:

```bash
cd java
../gen/openapi/autoupdate.sh
```
