package exception;
//Classe tem o objetivo de tratar exceções para devolver mensagens de erros mais utéis e limpas


import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationFormat {
	public static String formatarErros(BindingResult bindingResult) {
		JSONObject jsonObject = null;
		JSONArray jsonArray = new JSONArray();
		
		List<FieldError> fieldErrorList = bindingResult.getFieldErrors();//lista de campos com erros
		
		for (FieldError erro: fieldErrorList) {
			jsonObject = new JSONObject().put("campo", erro.getField());
			//jsonObject.put("valor", erro.getRejectedValue());
			jsonObject.put("mensagem", erro.getDefaultMessage());
			jsonArray.put(jsonObject);
		}
		
		JSONObject json = new JSONObject().put("Erros", jsonArray);
		return json.toString();
	}
}
