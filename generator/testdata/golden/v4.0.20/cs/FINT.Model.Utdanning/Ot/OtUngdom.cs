using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Ot
{
	public class OtUngdom {
		public enum Relasjonsnavn
        {
			PERSON,
			STATUS,
			ENHET,
			PROGRAMOMRADE
        }
        
	
		public Identifikator SystemId { get; set; }
		
	}
}
