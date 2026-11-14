import test from "node:test";
import assert from "node:assert/strict";
import { readFile } from "node:fs/promises";

const registry = JSON.parse(
  await readFile(new URL("../config/language-resource-registry.json", import.meta.url), "utf8"),
);

test("language resource registry is fail-closed", () => {
  assert.equal(registry.policy.default, "deny");
  assert.equal(registry.policy.public_availability_is_permission, false);
  assert.equal(registry.policy.pending_or_reference_only_is_training_data, false);
  assert.equal(registry.policy.automatic_gold_promotion, false);
});

test("every source records the rights fields required for review", () => {
  const required = [
    "permission_status",
    "rights_reviewed",
    "allowed_uses",
    "training_allowed",
    "redistribution_allowed",
    "commercial_use_allowed",
    "withdrawal_method_confirmed",
    "privacy_review_status",
  ];

  assert.ok(registry.sources.length > 0);
  for (const source of registry.sources) {
    assert.ok(source.id);
    assert.ok(source.name);
    for (const field of required) {
      assert.ok(Object.hasOwn(source, field), `${source.id} is missing ${field}`);
    }
  }
});

test("no unreviewed or pending source is enabled for training", () => {
  for (const source of registry.sources) {
    if (!source.rights_reviewed || source.permission_status === "pending") {
      assert.equal(source.training_allowed, false, `${source.id} must remain blocked`);
    }
  }
});

test("GiellaLT app-store exception is not treated as model-training permission", () => {
  const giella = registry.sources.find((source) => source.id === "giellalt-lang-kal");
  assert.ok(giella);
  assert.equal(giella.training_allowed, false);
  assert.equal(giella.redistribution_allowed, false);
  assert.ok(giella.allowed_uses.includes("app_store_distribution_subject_to_gpl"));
  assert.ok(!giella.allowed_uses.includes("training"));
});
