# Coupon Management API

# Definition

## Campaign Table

| Property    | Type                                      | Required | Description |
|-------------|-------------------------------------------|----------|-------------|
| id          | uuid                                      | true     |
| name        | String                                    | true     |
| description | String                                    | false    |
| expiresAt   | Date                                      | false    |
| active      | Boolean                                   | true     |
| cumulative  | Boolean                                   | true     |
| style       | [CampaignStyle](#campaignstyle-enum) | true     |
| rules       | uuid (Foreign Key to table [CampaignRules](#campaignrules-table)) | false    |
| blockList   | String[]                                  | false    | Won't allow sales with an externalId that is included on this list
| createdAt   | Date                                      | true     |

## CampaignStyle Enum

| Type          | Description                                                                   |
|---------------|-------------------------------------------------------------------------------|
| UniqueUsage   | Creates a new unique use coupon for each new sale based on the campaign.rules |
| SharedUsage   | It will use an already existing coupon associated with the campaign, if there is more than one coupon associated and its not an cumulative campaign it will use the highest coupon.priority property, in the case there is more than one coupon with the same priority it will choose the one with the least amount of associated sales, otherwise it will apply all available coupons in order to give the highest possible discount combination taking into account that percentage coupons won't add up to each other, just apply over the result from the last one |

## CampaignRules Table

| Property  | Type                                     | Required | Description |
|-----------|------------------------------------------|----------|-------------|
| id        | uuid                                     | true     |
| expiresAt | Date                                     | false    |
| lifetime  | Number                                   | false    | In Seconds  |
| type      | [DiscountType](#discounttype-enum)  | true     | 
| amount    | Number                                   | true     |
| prefix    | String                                   | true     |
| maxUsages | Number                                   | false    |

## DiscountType Enum

| Type       | Description                                                                |
|------------|----------------------------------------------------------------------------|
| Percentage | Percentage in decimal, e.g: 5% will be represented as 0.05 in the document |
| Fixed      | Fixed Value, e.g: $100.00 will be represented as 100                       |

## Coupon Table

| Property   | Type                                    | Required |
|------------|-----------------------------------------|----------|
| _id        | uuid                                    | true     |
| campaign   | uuid (Foreign Key to table [Campaign](#campaign-table))                      | true     |
| code       | String                                  | true     |
| active     | Boolean                                 | true     |
| priority   | Number                                  | true     |
| expiresAt  | Date                                    | true     |
| type       | [DiscountType](#discounttype-enum) | true     |
| amount     | Number                                  | true     |
| maxUsages  | Number                                  | false    |
| createdAt  | Date                                    | true     |

## SaleCrossRefereceCoupon Table

| Property  | Type               | Required  |
|-----------|--------------------|-----------|
| id        | uuid               | true      |
| coupon    | uuid (Foreign Key to table [Coupon](#coupon-table)) | true      |
| sale      | uuid (Foreign Key to table [Sale](#sale-table)) | true      |
| createdAt | Date               | true      |

## Sale Table

| Property   | Type                                   | Required | Description                      |
|------------|----------------------------------------|----------|----------------------------------|
| id         | uuid                                   | true     |
| externalId | String                                 | true     |
| total      | Number                                 | true     | Original Value                   |
| discounts  | Number                                 | true     | Total Discount                   |
| createdAt  | Date                                   | true     |