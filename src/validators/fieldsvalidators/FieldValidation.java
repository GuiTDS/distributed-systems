package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public interface FieldValidation {
    public boolean validate(JsonObject message);

    public String getErrorMessage();

}
