using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{

    public abstract class MappeResource 
    {

    
        public DateTime? AvsluttetDato { get; set; }
        public string Beskrivelse { get; set; }
        public List<KlasseResource> Klasse { get; set; }
        public Identifikator MappeId { get; set; }
        public List<MerknadResource> Merknad { get; set; }
        public List<string> Noekkelord { get; set; }
        public string OffentligTittel { get; set; }
        public DateTime? OpprettetDato { get; set; }
        public List<PartResource> Part { get; set; }
        public SkjermingResource Skjerming { get; set; }
        public Identifikator SystemId { get; set; }
        public string Tittel { get; set; }
        
        protected MappeResource()
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
     
            

        public void AddArkivdel(Link link)
        {
            AddLink("arkivdel", link);
        }

        public void AddAvsluttetAv(Link link)
        {
            AddLink("avsluttetAv", link);
        }

        public void AddOpprettetAv(Link link)
        {
            AddLink("opprettetAv", link);
        }
    }
}
