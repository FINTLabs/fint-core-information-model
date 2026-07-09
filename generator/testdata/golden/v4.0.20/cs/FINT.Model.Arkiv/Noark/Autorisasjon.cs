using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{
	public class Autorisasjon {
		public enum Relasjonsnavn
        {
			TILGANGSRESTRIKSJON,
			ADMINISTRATIVENHET,
			ARKIVRESSURS
        }
        
	
		public Identifikator SystemId { get; set; }
		
	}
}
