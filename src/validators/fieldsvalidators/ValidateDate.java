package validators.fieldsvalidators;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.google.gson.JsonObject;

public class ValidateDate extends Field implements FieldValidation {

    @Override
    public boolean validate(JsonObject message) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(message.get("data").getAsString(),formatter));
            return true;
        }catch(DateTimeParseException e) {
            super.errorMessage = "Data nao esta no padrao!";
            return false;
        }
        catch(NullPointerException e) {
			super.errorMessage = "O campo data nao foi enviado!";
			return false;
		}
		catch (UnsupportedOperationException e) {
			super.errorMessage = "O json possui campos nulos!";
			return false;
		} catch (NumberFormatException e1) {
			super.errorMessage = "Formato de dados invalidos!";
			return false;
		}
    }

}
