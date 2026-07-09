using System;
using System.Collections.Generic;



using FINT.Model.Utdanning.Vurdering;
using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Elev
{
	public class Elevforhold : Utdanningsforhold {
		public enum Relasjonsnavn
        {
			ELEV,
			KATEGORI,
			SKOLE,
			AVBRUDDSARSAK,
			FRAVARSREGISTRERINGER,
			FAGGRUPPEMEDLEMSKAP,
			SKOLEAR,
			UNDERVISNINGSGRUPPEMEDLEMSKAP,
			PERSONGRUPPEMEDLEMSKAP,
			EKSAMENSGRUPPEMEDLEMSKAP,
			KONTAKTLARERGRUPPEMEDLEMSKAP,
			ELEVFRAVAR,
			TILRETTELEGGING,
			ELEVVURDERING,
			PROGRAMOMRADEMEDLEMSKAP,
			KLASSEMEDLEMSKAP
        }
        
	
		public List<Anmerkninger> Anmerkninger { get; set; }
		public DateTime? Avbruddsdato { get; set; }
		public Periode Gyldighetsperiode { get; set; }
		public bool? Hovedskole { get; set; }
		public bool? TosprakligFagopplaring { get; set; }
		
	}
}
