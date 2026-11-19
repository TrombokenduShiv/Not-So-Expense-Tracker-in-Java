# Mathematical notes

## Money rounding

Amounts are decimal values quantized to two decimal places using HALF_EVEN. This avoids binary floating-point representation error for accounting values. Floating point is reserved for statistical simulation.

## Welford online variance

For observation `x_n`, update `delta = x_n - mean_(n-1)`, `mean_n = mean_(n-1) + delta/n`, and `M2_n = M2_(n-1) + delta * (x_n - mean_n)`. Sample variance is `M2/(n-1)`, standard deviation its square root. It is one-pass and O(1) memory.

## EWMA

`m_t = alpha*x_t + (1-alpha)*m_(t-1)`, where `0 < alpha <= 1`. Larger alpha reacts faster; smaller alpha smooths noise.

## Bootstrap and shrinkage

Bootstrap resamples observed daily values from a matching weekday bucket, preserving empirical skew and large observations rather than imposing a Gaussian distribution. Shrinkage combines a category estimate and pooled prior: `w=n/(n+k)` and `estimate=w*local+(1-w)*prior`. With `k=10`, small samples are pulled more strongly toward the prior.

## Monte Carlo

Each path starts at balance `B_0` and subtracts simulated daily outflow: `B_t=B_(t-1)-S_t`. Breach probability is the fraction of paths with any `B_t < 0`. The empirical P10/P50/P90 at day `t` are order-statistic quantiles across paths. Approximate standard error of a breach estimate is `sqrt(p(1-p)/N)`; a rough 95% interval is estimate ± `1.96*SE` (often called a 2-sigma Monte Carlo error). Seeded independent random streams make repeated runs reproducible.

## Hash chain

`H_i = SHA256(H_(i-1) || canonical(event_i))`, with a 64-zero genesis. Changing a payload or predecessor invalidates that row and all later links.

## Settlement complexity

A greedy creditor/debtor matching repeatedly transfers the smaller absolute balance between the largest creditor and debtor. It terminates in at most `n-1` transfers because each transfer settles at least one participant. Globally minimum transfer settlement is NP-hard in general; this greedy approach is fast and explainable but not guaranteed globally optimal.
