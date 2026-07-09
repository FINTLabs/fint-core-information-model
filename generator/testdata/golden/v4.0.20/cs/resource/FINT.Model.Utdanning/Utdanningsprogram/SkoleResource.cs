using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Utdanning.Utdanningsprogram
{

    public class SkoleResource : EnhetResource 
    {

    
        public string Domenenavn { get; set; }
        public string JuridiskNavn { get; set; }
        public string Navn { get; set; }
        public Identifikator Skolenummer { get; set; }
        public Identifikator SystemId { get; set; }
        
            

        public void AddOrganisasjon(Link link)
        {
            AddLink("organisasjon", link);
        }

        public void AddSkoleeierType(Link link)
        {
            AddLink("skoleeierType", link);
        }

        public void AddVigoreferanse(Link link)
        {
            AddLink("vigoreferanse", link);
        }

        public void AddElevforhold(Link link)
        {
            AddLink("elevforhold", link);
        }

        public void AddKlasse(Link link)
        {
            AddLink("klasse", link);
        }

        public void AddKontaktlarergruppe(Link link)
        {
            AddLink("kontaktlarergruppe", link);
        }

        public void AddSkoleressurs(Link link)
        {
            AddLink("skoleressurs", link);
        }

        public void AddUndervisningsforhold(Link link)
        {
            AddLink("undervisningsforhold", link);
        }

        public void AddFag(Link link)
        {
            AddLink("fag", link);
        }

        public void AddFaggruppe(Link link)
        {
            AddLink("faggruppe", link);
        }

        public void AddUndervisningsgruppe(Link link)
        {
            AddLink("undervisningsgruppe", link);
        }

        public void AddEksamensgruppe(Link link)
        {
            AddLink("eksamensgruppe", link);
        }

        public void AddUtdanningsprogram(Link link)
        {
            AddLink("utdanningsprogram", link);
        }
    }
}
