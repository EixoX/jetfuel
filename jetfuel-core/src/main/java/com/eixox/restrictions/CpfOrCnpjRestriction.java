package com.eixox.restrictions;

public class CpfOrCnpjRestriction implements Restriction {

	public CpfOrCnpjRestriction() {
	}

	public CpfOrCnpjRestriction(CpfOrCnpj cpfOrCnpj) {
		// just complying to a constructor pattern.
	}

	public static boolean isValid(long value) {
		return CpfRestriction.isValid(value) || CnpjRestriction.isValid(value);
	}

	public RestrictionResult validate(Object input) {
		if (input == null)
			return new RestrictionResult(true, "");
		else if (input instanceof String) {
			String is = (String) input;
			if (is.isEmpty())
				return new RestrictionResult(true, "");
			else
				return isValid(Long.parseLong(is))
						? new RestrictionResult(true, "")
						: new RestrictionResult(false, "N�o � um CPF ou CNPJ v�lido.");
		} else if (input instanceof Number) {
			return isValid(((Number) input).longValue())
					? new RestrictionResult(true, "")
					: new RestrictionResult(false, "N�o � um CPF ou CNPJ v�lido.");
		} else {
			return new RestrictionResult(false, "N�o foi poss�vel converter para um CPF ou CNPJ.");
		}
	}

	@Override
	public String toString() {
		return "CpfOrCnpj()";
	}

}
