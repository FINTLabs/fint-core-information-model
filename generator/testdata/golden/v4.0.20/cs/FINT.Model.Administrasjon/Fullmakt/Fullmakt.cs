using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Administrasjon.Fullmakt
{
	public class Fullmakt {
		public enum Relasjonsnavn
        {
			RAMME,
			FUNKSJON,
			OBJEKT,
			ORGANISASJONSELEMENT,
			ART,
			ANLEGG,
			DIVERSE,
			AKTIVITET,
			ANSVAR,
			STEDFORTREDER,
			KONTRAKT,
			FULLMEKTIG,
			PROSJEKT,
			FORMAL,
			ROLLE,
			LOPENUMMER
        }
        
	
		public Periode Gyldighetsperiode { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
