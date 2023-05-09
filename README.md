# Coupon Management API

# Definition

## Campaign Table

| Field          | Type                                                    | Required | Description                                     |
|----------------|---------------------------------------------------------|----------|-------------------------------------------------|
| id             | uuid                                                    | true     |                                                 |
| name           | varchar(200)                                            | true     |                                                 |
| description    | text                                                    | false    |                                                 |
| expiresAt      | timestamp                                               | false    |                                                 |
| startsAt       | timestamp                                               | true     | default now                                     |
| active         | bool                                                    | true     | don't override the expiresAt and startsAt logic |
| cumulative     | bool                                                    | true     |                                                 |
| style          | [Campaign Style Enum](#campaignstyle-enum)              | true     |                                                 |
| campaignRuleId | uuid - Foreign Key [Campaign Rule](#campaignrule-table) | false    | required if campaign style is UniqueUsage       |
| createdAt      | timestamp                                               | true     |                                                 |


### CampaignStyle Enum

| Type          | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| UniqueUsage   | Creates a new unique use coupon for each new sale                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| SharedUsage   | It will use an already existing coupon associated with the campaign, if there is more than one coupon associated and its not an cumulative campaign it will use the highest coupon.priority property, in the case there is more than one coupon with the same priority it will choose the one with the least amount of associated sales, otherwise it will apply all available coupons in order to give the highest possible discount combination taking into account that percentage coupons won't add up to each other, just apply over the result from the last one | 

## CampaignRule Table

| Field     | Type                                     | Required | Description |
|-----------|------------------------------------------|----------|-------------|
| id        | uuid                                     | true     |             |
| lifetime  | Number                                   | false    | In Seconds  |
| type      | [Discount Type Enum](#discounttype-enum) | true     |             |
| value     | Number                                   | true     |             |
| prefix    | String                                   | true     |             |
| maxUsages | Number                                   | false    |             |

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
| priority  | Number                                         | true     |
| expiresAt | Date                                           | true     |
| type      | [Discount Type Enum](#discounttype-enum)       | true     |
| amount    | Number                                         | true     |
| maxUsages | Number                                         | false    |
| createdAt | Date                                           | true     |

## Sale Table

| Field      | Type        | Required | Description                     |
|------------|-------------|----------|---------------------------------|
| id         | uuid        | true     |                                 |
| externalId | varchar(50) | true     |                                 |
| origin     | varchar(50) | true     | Sale Origin                     |
| total      | money       | true     | Original Value                  |
| discounts  | money       | true     | Total Discount                  |
| sellValue  | money       | true     | Original Value - Total Discount |
| createdAt  | timestamp   | true     |                                 |

## BlockList Table

| Field      | Type                                                    | Required | Description |
|------------|---------------------------------------------------------|----------|-------------|
| id         | uuid                                                    | true     |             |
| campaignId | uuid - Foreign Key to table [Campaign](#campaign-table) | true     |             |
| value      | varchar(50)                                             | true     |             |
| type       | [Block Type Enum](#blocktype-enum)                      | true     |             |

### BlockType Enum

| Type       | Description                              |
|------------|------------------------------------------|
| externalId | won't allow sale based on external id    |
| origin     | won't allow sale based on sales's origin |