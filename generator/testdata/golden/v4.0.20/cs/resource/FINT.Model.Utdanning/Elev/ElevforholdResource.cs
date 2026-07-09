using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Utdanning.Vurdering;
using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Elev
{

    public class ElevforholdResource : Utdanningsforhold 
    {

    
        public List<AnmerkningerResource> Anmerkninger { get; set; }
        public DateTime? Avbruddsdato { get; set; }
        public Periode Gyldighetsperiode { get; set; }
        public bool? Hovedskole { get; set; }
        public bool? TosprakligFagopplaring { get; set; }
        
        public ElevforholdResource()
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
     
            

        public void AddElev(Link link)
        {
            AddLink("elev", link);
        }

        public void AddKategori(Link link)
        {
            AddLink("kategori", link);
        }

        public void AddSkole(Link link)
        {
            AddLink("skole", link);
        }

        public void AddAvbruddsarsak(Link link)
        {
            AddLink("avbruddsarsak", link);
        }

        public void AddFravarsregistreringer(Link link)
        {
            AddLink("fravarsregistreringer", link);
        }

        public void AddFaggruppemedlemskap(Link link)
        {
            AddLink("faggruppemedlemskap", link);
        }

        public void AddSkolear(Link link)
        {
            AddLink("skolear", link);
        }

        public void AddUndervisningsgruppemedlemskap(Link link)
        {
            AddLink("undervisningsgruppemedlemskap", link);
        }

        public void AddPersongruppemedlemskap(Link link)
        {
            AddLink("persongruppemedlemskap", link);
        }

        public void AddEksamensgruppemedlemskap(Link link)
        {
            AddLink("eksamensgruppemedlemskap", link);
        }

        public void AddKontaktlarergruppemedlemskap(Link link)
        {
            AddLink("kontaktlarergruppemedlemskap", link);
        }

        public void AddElevfravar(Link link)
        {
            AddLink("elevfravar", link);
        }

        public void AddTilrettelegging(Link link)
        {
            AddLink("tilrettelegging", link);
        }

        public void AddElevvurdering(Link link)
        {
            AddLink("elevvurdering", link);
        }

        public void AddProgramomrademedlemskap(Link link)
        {
            AddLink("programomrademedlemskap", link);
        }

        public void AddKlassemedlemskap(Link link)
        {
            AddLink("klassemedlemskap", link);
        }
    }
}
