using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Administrasjon.Personal
{

    public class PersonalressursResource 
    {

    
        public Identifikator Ansattnummer { get; set; }
        public Periode Ansettelsesperiode { get; set; }
        public DateTime? Ansiennitet { get; set; }
        public Identifikator Brukernavn { get; set; }
        public string Jobbtittel { get; set; }
        public Kontaktinformasjon Kontaktinformasjon { get; set; }
        public Identifikator SystemId { get; set; }
        
        public PersonalressursResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddPersonalressurskategori(Link link)
        {
            AddLink("personalressurskategori", link);
        }

        public void AddArbeidsforhold(Link link)
        {
            AddLink("arbeidsforhold", link);
        }

        public void AddPerson(Link link)
        {
            AddLink("person", link);
        }

        public void AddStedfortreder(Link link)
        {
            AddLink("stedfortreder", link);
        }

        public void AddFullmakt(Link link)
        {
            AddLink("fullmakt", link);
        }

        public void AddLeder(Link link)
        {
            AddLink("leder", link);
        }

        public void AddPersonalansvar(Link link)
        {
            AddLink("personalansvar", link);
        }

        public void AddSkoleressurs(Link link)
        {
            AddLink("skoleressurs", link);
        }
    }
}
