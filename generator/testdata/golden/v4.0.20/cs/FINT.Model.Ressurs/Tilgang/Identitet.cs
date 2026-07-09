using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Tilgang
{
	public class Identitet {
		public enum Relasjonsnavn
        {
			PERSONALRESSURS,
			RETTIGHET
        }
        
	
		public Identifikator SystemId { get; set; }
		
	}
}
