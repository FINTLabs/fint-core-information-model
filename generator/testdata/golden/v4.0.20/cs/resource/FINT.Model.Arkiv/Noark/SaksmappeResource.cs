using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Arkiv.Noark
{

    public abstract class SaksmappeResource : MappeResource 
    {

    
        public List<JournalpostResource> Journalpost { get; set; }
        public string Saksaar { get; set; }
        public DateTime? Saksdato { get; set; }
        public string Sakssekvensnummer { get; set; }
        public DateTime? UtlaantDato { get; set; }
        
            

        public void AddSaksmappetype(Link link)
        {
            AddLink("saksmappetype", link);
        }

        public void AddSaksstatus(Link link)
        {
            AddLink("saksstatus", link);
        }

        public void AddTilgangsgruppe(Link link)
        {
            AddLink("tilgangsgruppe", link);
        }

        public void AddJournalenhet(Link link)
        {
            AddLink("journalenhet", link);
        }

        public void AddAdministrativEnhet(Link link)
        {
            AddLink("administrativEnhet", link);
        }

        public void AddSaksansvarlig(Link link)
        {
            AddLink("saksansvarlig", link);
        }
    }
}
