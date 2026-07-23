# Skipped cross-service files (market domain migration)

These 4 source files from `building-old/scm-goods` were NOT migrated into
`biz/market` because they couple directly to **goods-internal** types or live
Dubbo facades. They are archived here (original `com.zkl.scm.goods.*` form,
untransformed) and deferred until the goods bounded-context exposes a proper
port / the application layer is built.

| File | Reason skipped |
|------|----------------|
| `MarketSpuVO.java` | Uses goods-internal `SpuVO` (goods model, not a market read-model). |
| `SpuCategoryDAO.java` | Operates on goods-internal `SpuCategoryDO`/`SpuCategoryVO`. |
| `DistributionRepositoryImpl.java` | `@DubboReference ISpuFacade/IStoreFacade` + goods `SpuDAO`/`SpuDO`. |
| `GoodsRelationRepositoryImpl.java` | goods-internal `SpuDAO`/`SpuDO`. |

## Impact on kept code

- Domain ports `IDistributionRepository` and `IGoodsRelationRepository` were
  migrated (interfaces compile independently). Their **implementations** are the
  two skipped `*RepositoryImpl` files — Spring wiring for these two beans is
  therefore absent until re-implemented against a goods port.
- `MarketCategoryRepositoryImpl` and `MarketRepositoryImpl` were migrated fully
  (no goods coupling).

## Re-integration path

When goods exposes a stable port (e.g. `SpuQueryApi` in `biz-goods-facade`),
re-implement the two repository impls under
`biz-market-infrastructure/.../repository` against that port + local RPC
read-models in `...market.model.rpc.*`, and drop `MarketSpuVO` in favour of a
local projection. No `biz-goods` internal package may be imported.
