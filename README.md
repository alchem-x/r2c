# R2 CLI

## Build

```sh
./gradlew build
```

## Setup `.env`

```sh
export R2_ACCESS_KEY="access_key"
export R2_SECRET_KEY="secret_key"
export R2_ENDPOINT="endpoint"
export R2_BUCKET="bucket"
export R2_DIST="dist"
```

## Usage

### Get Object

```sh
./run.sh get-object $OBJECT_KEY
```

### Put Object

```sh
./run.sh put-object $OBJECT_KEY $FILEPATH
```

### Delete Object

```sh
./run.sh delete-object $OBJECT_KEY
```
