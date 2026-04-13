package org.example.Parser;

import com.google.gson.*;
import org.example.Model.User.FreePlan;
import org.example.Model.User.Plan;
import org.example.Model.User.PremiumBasePlan;
import org.example.Model.User.PremiumTopPlan;

import java.lang.reflect.Type;

public class PlanAdapter implements JsonSerializer<Plan>, JsonDeserializer<Plan> {

    @Override
    public JsonElement serialize(Plan src, Type typeOfSrc, JsonSerializationContext context) {
        if (src instanceof FreePlan) {
            return new JsonPrimitive("FreePlan");
        } else if (src instanceof PremiumBasePlan) {
            return new JsonPrimitive("PremiumBasePlan");
        } else if (src instanceof PremiumTopPlan) {
            return new JsonPrimitive("PremiumTopPlan");
        } else {
            throw new JsonParseException("Tipo de plano desconhecido: " + src.getClass().getName());
        }
    }

    @Override
    public Plan deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String planType = json.getAsString();

        switch (planType) {
            case "FreePlan":
                return new FreePlan();
            case "PremiumBasePlan":
                return new PremiumBasePlan();
            case "PremiumTopPlan":
                return new PremiumTopPlan();
            default:
                throw new JsonParseException("Tipo de plano desconhecido: " + planType);
        }
    }
}