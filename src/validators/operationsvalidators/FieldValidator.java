package validators.operationsvalidators;

import java.util.List;
import com.google.gson.JsonObject;

import validators.fieldsvalidators.FieldValidation;

public class FieldValidator extends validators.Validator {
    private JsonObject receivedMessage;
    private List<FieldValidation> validations;
    private boolean hasError;
    public FieldValidator(JsonObject receivedMessage, List<FieldValidation> validations) {
        this.receivedMessage = receivedMessage;
        this.validations = validations;
        hasError = false;
    }

    @Override
    public boolean isValid() {
        validations.forEach(validation -> {
            if(!validation.validate(receivedMessage)) {
                super.errorMessage = validation.getErrorMessage();
                this.hasError = true;
                return;
            }
        });
        if(this.hasError) {
            super.opResponse = super.getFailOpCode();
            return false;
        }
        return true;
    }
}
