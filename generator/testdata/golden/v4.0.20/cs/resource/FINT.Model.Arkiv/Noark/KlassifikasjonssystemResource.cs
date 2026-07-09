using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{

    public class KlassifikasjonssystemResource 
    {

    
        public string AvsluttetAv { get; set; }
        public DateTime? AvsluttetDato { get; set; }
        public string Beskrivelse { get; set; }
        public List<KlasseResource> Klasse { get; set; }
        public string OpprettetAv { get; set; }
        public DateTime OpprettetDato { get; set; }
        public Identifikator SystemId { get; set; }
        public string Tittel { get; set; }
        
        public KlassifikasjonssystemResource()
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
     
            

        public void AddKlassifikasjonstype(Link link)
        {
            AddLink("klassifikasjonstype", link);
        }

        public void AddArkivdel(Link link)
        {
            AddLink("arkivdel", link);
        }
    }
}
