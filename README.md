# Slow Query Detector

This application demonstrates slow query scenarios in a Spring Boot application with Hibernate.

## Endpoints

### 1. Generate Users
Generates a specified number of users in the database. Default is 1 million users.

```bash
# Generate 1 million users (default)
curl -X POST "http://localhost:8080/api/users/generate"

# Generate custom number of users
curl -X POST "http://localhost:8080/api/users/generate?count=500000"
```

### 2. Search Users by Exact Name
Searches for users with an exact name match.

```bash
# Search for users with exact name "John Doe"
curl "http://localhost:8080/api/users/search/exact?name=John%20Doe"
```

### 3. Search Users by Name Contains
Searches for users whose name contains the given string (case-insensitive).

```bash
# Search for users whose name contains "john"
curl "http://localhost:8080/api/users/search/contains?name=john"
```

### 4. Search Users by Name Starts With
Searches for users whose name starts with the given string (case-insensitive).

```bash
# Search for users whose name starts with "john"
curl "http://localhost:8080/api/users/search/starts-with?name=john"
```

## Example Usage

1. First, generate some users:
```bash
curl -X POST "http://localhost:8080/api/users/generate?count=1000000"
```

2. Then try different search patterns:
```bash
# Exact match
curl "http://localhost:8080/api/users/search/exact?name=John%20Smith"

# Contains search
curl "http://localhost:8080/api/users/search/contains?name=john"

# Starts with search
curl "http://localhost:8080/api/users/search/starts-with?name=john"
```

## Notes

- The name column is intentionally not indexed to demonstrate slow query scenarios
- All searches are performed on the name field
- The generate endpoint uses batch processing for better performance
- Progress is logged during user generation