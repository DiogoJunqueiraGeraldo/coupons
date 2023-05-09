# Coupon Management API

# Setup database

```sh
docker exec -it postgres bash
psql -U user
\l 
CREATE DATABASE coupons;
\l
# Execute Spring Application
\c coupons
\d
```

# Definition

## Campaign Table

| Field        | Type                                                    | Required | Description                                     |
|--------------|---------------------------------------------------------|----------|-------------------------------------------------|
| id           | uuid                                                    | true     |                                                 |
| name         | varchar(200)                                            | true     |                                                 |
| description  | varchar(500)                                            | false    |                                                 |
| expiresAt    | timestamp                                               | false    |                                                 |
| startsAt     | timestamp                                               | true     | default now                                     |
| active       | bool                                                    | true     | don't override the expiresAt and startsAt logic |
| cumulative   | bool                                                    | true     |                                                 |
| style        | [Campaign Style Enum](#campaignstyle-enum)              | true     |                                                 |
| campaignRule | uuid - Foreign Key [Campaign Rule](#campaignrule-table) | false    | required if campaign style is UniqueUsage       |
| createdAt    | timestamp                                               | true     |                                                 |


### CampaignStyle Enum

| Type          | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| UniqueUsage   | Creates a new unique use coupon for each new sale                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| SharedUsage   | It will use an already existing coupon associated with the campaign, if there is more than one coupon associated and its not an cumulative campaign it will use the highest coupon.priority property, in the case there is more than one coupon with the same priority it will choose the one with the least amount of associated sales, otherwise it will apply all available coupons in order to give the highest possible discount combination taking into account that percentage coupons won't add up to each other, just apply over the result from the last one | 

## CampaignRule Table

| Field     | Type                                     | Required | Description |
|-----------|------------------------------------------|----------|-------------|
| id        | uuid                                     | true     |             |
| lifetime  | int                                      | false    | In Seconds  |
| type      | [Discount Type Enum](#discounttype-enum) | true     |             |
| value     | Decimal                                  | true     |             |
| prefix    | varchar(6)                               | true     |             |
| maxUsages | int                                      | false    |             |

### DiscountType Enum

| Type       | Description                                                                |
|------------|----------------------------------------------------------------------------|
| Percentage | Percentage in decimal, e.g: 5% will be represented as 0.05 in the document |
| Fixed      | Fixed Value, e.g: $100.00 will be represented as 100                       |


## Coupon Table

| Field     | Type                                           | Required |
|-----------|------------------------------------------------|----------|
| id        | uuid                                           | true     |
| campaign  | uuid - Foreign Key [Campaign](#campaign-table) | true     |
| code      | String                                         | true     |
| active    | Boolean                                        | true     |
| priority  | int                                            | true     |
| expiresAt | Timestamp                                      | true     |
| type      | [Discount Type Enum](#discounttype-enum)       | true     |
| value     | decimal                                        | true     |
| maxUsages | decimal                                        | false    |
| createdAt | Timestamp                                      | true     |
| updatedAt | Timestamp                                      | true     |

## Sale Table

| Field      | Type        | Required | Description                     |
|------------|-------------|----------|---------------------------------|
| id         | uuid        | true     |                                 |
| externalId | varchar(50) | true     |                                 |
| origin     | varchar(50) | true     | Sale Origin                     |
| total      | decimal     | true     | Original Value                  |
| discounts  | decimal     | true     | Total Discount                  |
| sellValue  | decimal     | true     | Original Value - Total Discount |
| createdAt  | timestamp   | true     |                                 |
| updatedAt  | timestamp   | true     |                                 |

## SaleBlocker Table

| Field      | Type                                                    | Required | Description |
|------------|---------------------------------------------------------|----------|-------------|
| id         | uuid                                                    | true     |             |
| campaignId | uuid - Foreign Key to table [Campaign](#campaign-table) | true     |             |
| value      | varchar(50)                                             | true     |             |
| type       | [Block Type Enum](#blocktype-enum)                      | true     |             |

### BlockType Enum

| Type       | Description                              |
|------------|------------------------------------------|
| ExternalId | won't allow sale based on external id    |
| Origin     | won't allow sale based on sales's origin |