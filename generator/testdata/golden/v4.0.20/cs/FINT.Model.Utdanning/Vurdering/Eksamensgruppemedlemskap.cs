using System;
using System.Collections.Generic;



using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Vurdering
{
	public class Eksamensgruppemedlemskap : Gruppemedlemskap {
		public enum Relasjonsnavn
        {
			DELEGERTTIL,
			ELEVFORHOLD,
			FORETRUKKETSKOLE,
			EKSAMENSGRUPPE,
			NUS,
			BETALINGSSTATUS,
			FORETRUKKETSENSOR
        }
        
	
		public bool? Delegert { get; set; }
		public string Kandidatnummer { get; set; }
		
	}
}
