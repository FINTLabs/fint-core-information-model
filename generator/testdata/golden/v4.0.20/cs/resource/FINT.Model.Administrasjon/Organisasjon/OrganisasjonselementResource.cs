using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Administrasjon.Organisasjon
{

    public class OrganisasjonselementResource : EnhetResource 
    {

    
        public Periode Gyldighetsperiode { get; set; }
        public string Kortnavn { get; set; }
        public string Navn { get; set; }
        public Identifikator OrganisasjonsId { get; set; }
        public Identifikator OrganisasjonsKode { get; set; }
        
            

        public void AddAnsvar(Link link)
        {
            AddLink("ansvar", link);
        }

        public void AddOrganisasjonstype(Link link)
        {
            AddLink("organisasjonstype", link);
        }

        public void AddLeder(Link link)
        {
            AddLink("leder", link);
        }

        public void AddOverordnet(Link link)
        {
            AddLink("overordnet", link);
        }

        public void AddUnderordnet(Link link)
        {
            AddLink("underordnet", link);
        }

        public void AddSkole(Link link)
        {
            AddLink("skole", link);
        }

        public void AddArbeidsforhold(Link link)
        {
            AddLink("arbeidsforhold", link);
        }
    }
}
