# API Examples

Base URL:

```text
http://localhost:8081/api
```

## Auth

Register:

```bash
curl -X POST http://localhost:8081/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"charlie\",\"email\":\"charlie@example.edu\",\"password\":\"123456\",\"nickname\":\"Charlie\",\"studentNo\":\"20260003\",\"campus\":\"Main Campus\",\"phone\":\"13800000003\"}"
```

Login:

```bash
curl -X POST http://localhost:8081/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"alice\",\"password\":\"123456\"}"
```

Use the token from `data.token`:

```bash
set TOKEN=your-jwt-token
```

## Products

Create draft product:

```bash
curl -X POST http://localhost:8081/api/products ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TOKEN%" ^
  -d "{\"title\":\"Calculus Notes\",\"description\":\"Complete notes for first-year calculus.\",\"price\":15.00,\"category\":\"Books\",\"conditionText\":\"Good\",\"tradeLocation\":\"Library\"}"
```

Publish product:

```bash
curl -X POST http://localhost:8081/api/products/1/publish ^
  -H "Authorization: Bearer %TOKEN%"
```

List visible products:

```bash
curl "http://localhost:8081/api/products?page=0&size=10"
```

Home hot products:

```bash
curl "http://localhost:8081/api/products/home"
```

Search products:

```bash
curl "http://localhost:8081/api/search/products?keyword=keyboard&page=0&size=10"
```

## Orders

Create order:

```bash
curl -X POST http://localhost:8081/api/orders ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TOKEN%" ^
  -d "{\"productId\":1,\"meetLocation\":\"Library Gate\",\"remark\":\"Can meet after class.\"}"
```

Seller confirms:

```bash
curl -X POST http://localhost:8081/api/orders/1/confirm ^
  -H "Authorization: Bearer %TOKEN%"
```

Complete trade:

```bash
curl -X POST http://localhost:8081/api/orders/1/complete ^
  -H "Authorization: Bearer %TOKEN%"
```

Cancel order:

```bash
curl -X POST http://localhost:8081/api/orders/1/cancel ^
  -H "Authorization: Bearer %TOKEN%"
```
